# MIGRATION_NOTES.md — состояние миграции на Compose Multiplatform

## Структура проекта теперь

- **`:app`** — старое Android-приложение (стек 2024-2025), **не тронуто**, рабочий бэкап как и просил.
- **`:shared`** — KMP-модуль: `commonMain` (общий код) + `androidMain` + `iosMain`.
- **`:composeApp`** — новое, отдельное Android-приложение (`applicationId = com.leafcellteam.mafia.compose`), которое реально запускает `:shared`. Можно ставить на телефон одновременно со старым `:app` и сравнивать.
- **`/mafia`** — Xcode-проект для iOS (пока не подключён к `:shared`, см. ниже "Что дальше").
- **`_old_backups/`** — мусор, который был в корне репозитория (случайные `git diff` дампы, старый zip-бэкап апреля 2025). Можно удалить, если не нужны.

## Что было не так и что исправлено

`:shared/commonMain` представлял собой копипасту файлов из `:app` почти без адаптации — большая часть просто не компилировалась бы под iOS (а местами и вообще нигде, т.к. не хватало базовой обвязки). Разобрано и исправлено:

- **Android-only API в commonMain** (`Context`, `android.util.Log`, `R.drawable/string`, `Patterns`, `SuppressLint`, `Build`, `LocalContext`, `androidx.activity.compose.BackHandler`) — обёрнуты в expect/actual (`PlatformContext`, `BackHandler`) либо вынесены в `androidMain`, где им и место (`NetworkMonitor`, `UpdateManager`/`UpdateDialog` — Google Play Update, `Local.kt` — Android-специфичное переключение локали).
- **`TokenPreferences`** — теперь expect/actual: Android — SharedPreferences (как раньше), iOS — NSUserDefaults. `TokenManager` не пришлось менять вообще.
- **Хранение языка (`DataStore`/`AppSettings`)** — переписано без прямой зависимости от `Context` (datastore-preferences реально мультиплатформенный, платформенной осталась только фабрика самого DataStore).
- **`java.util.Date`** (JVM-only) → `kotlinx.datetime.Instant` в моделях чата; **`SimpleDateFormat`** → ручное форматирование через `kotlinx.datetime.LocalDate`.
- **Gson (`@SerializedName`)** → `kotlinx.serialization.@SerialName` (15 файлов, 58 вхождений) — Gson недоступен на iOS.
- **Мёртвый код удалён**: `interseptor/*` (OkHttp-перехватчики, полностью заменены Ktor), старый `Retrofit`-интерфейс `HistoryApiService` (аннотации `@GET/@Query` убраны, интерфейс стал чистым контрактом, как и было задумано в `KtorHistoryApiService`).
- **Ресурсы**: `R.drawable.*` / `R.string.*` → `Res.drawable.*` / `Res.string.*` (сгенерированный класс Compose Resources, пакет явно зафиксирован в `shared/build.gradle.kts` как `com.leafcellteam.mafia.resources`, чтобы не гадать). `painterResource`/`stringResource`/`@Preview` — переключены с `androidx.compose.ui.*` на мультиплатформенные `org.jetbrains.compose.*`.
- **SignalR**: `WaitingRoom.kt`/`GameRoom.kt` обращались напрямую к Android-only `com.microsoft.signalr.HubConnectionState` вместо уже существующей общей обёртки `HubState` — поправлено.
- **Композиционный корень** (`AppContainer.kt`) — раньше `tokenManager`/`tokenPreferences` и весь граф вьюмоделей (`GameRoomServiceHub → GameRoomViewModel → SignalRServiceHub → WaitingRoomViewModel → RegisterViewModel`) нигде не были объявлены в `:shared`, из-за чего 9+ файлов не резолвились. Теперь собраны в одном месте.
- **Навигация**: `SharedApp()` был пустой заглушкой. Теперь — настоящий `NavHost` с type-safe маршрутами (`org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha10` — это версия, которую сам JetBrains официально сопоставляет с используемой в проекте Compose Multiplatform 1.7.3). Подключены `LogIn → MainMenu`, остальные экраны ведут на явную заглушку "не подключено" вместо краша.

## Важно про версии зависимостей

- В `libs.versions.toml` теперь **два** разных артефакта навигации:
  - `androidx-navigation-compose` (`androidx.navigation:navigation-compose:2.8.5`) — Android-only, из Google. Больше нигде не используется в `:shared`, можно вообще убрать, если не нужен где-то ещё.
  - `navigation-compose-multiplatform` (`org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha10`) — тот, что реально используется в `commonMain`. Это альфа-версия — так и есть у JetBrains для этой версии CMP, это нормально, не баг.
- **Обязательно сделай `./gradlew :shared:compileKotlinMetadata` (или Sync в Android Studio) перед тем, как читать код дальше** — я не смог прогнать реальную компиляцию (нет сети/Android SDK в моей песочнице), только ручной построчный аудит. Вероятность, что где-то всплывёт мелкая опечатка или несовпадение сигнатуры — не нулевая.

## Что НЕ сделано (следующие шаги)

1. **Остальные экраны не подключены в `SharedApp()`**: создание/присоединение к игре, комната ожидания, сама игра, профиль, магазин, история, достижения, настройки. Они уже лежат в `commonMain` и очищены от Android-кода (см. список файлов выше), но навигация к ним сейчас ведёт на заглушку "не подключено". Подключаются по той же схеме, что `LogIn`/`MainMenu` — добавить `@Serializable data object` в `Destination`, добавить `composable<...> { }` в `NavHost`.
2. **iOS-приложение (`/mafia`) не подключено к `:shared`** — Xcode-проект сейчас стандартный шаблон Apple, ничего не знает про `SharedApp()`. Нужно: (а) добавить iOS-таргет в схему сборки, чтобы Xcode подхватывал framework из `:shared`; (б) в Swift создать `UIViewControllerRepresentable`, вызывающий `MainViewController()` (эту Kotlin-функцию в `iosMain` тоже ещё нужно написать: `fun MainViewController() = ComposeUIViewController { SharedApp() }`); (в) вызвать `initApp(PlatformContext())` перед первым рендером.
3. **`composeApp/MainActivity.kt`** — уже написан и лежит в модуле (вызывает `initAndroidDataStore`, `initApp`, `setContent { SharedApp() }`). Проверь, что всё собирается.
4. **Динамический цвет (Material You, Android 12+)** убран из `Theme.kt` для кроссплатформенности — можно вернуть отдельно только для Android через expect/actual, если важно визуально.
5. **Смена языка на лету** (`onLanguageSelected` в `LogIn`/`MainMenu`) сейчас TODO-заглушка — нужно вызвать `AppSettings.saveLanguage(lang)` и применить локаль (на Android — через `Local.kt`, на iOS — другой механизм, см. `Local.kt`'s комментарий).
6. **Мелкая уборка** (не блокирует сборку): в `shared/build.gradle.kts` в `androidMain` всё ещё висят неиспользуемые `retrofit`/`converter-gson` — можно убрать, когда будет время всё перепроверить.
7. **Технический момент**: скрипт массовой правки (Gson/Preview/ресурсы/Log) нормализовал переносы строк CRLF→LF в ~83 файлах. Функционально не влияет, но `git diff` по этим файлам покажет "весь файл изменён" — это ожидаемо, не паникуй.
