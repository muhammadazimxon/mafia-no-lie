import org.jetbrains.kotlin.gradle.plugin.mpp.DisableCacheInKotlinVersion
tasks.register("printEnum") {
    doLast {
        println(DisableCacheInKotlinVersion.values().joinToString())
    }
}
