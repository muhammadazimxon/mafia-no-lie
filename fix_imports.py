import os

app_dir = '/Users/abduvoris.a.a./AndroidStudioProjects/CurrentProject/MafiaOnlineJetpackComposeCAPI/app/src/main/java/com/leafcellteam/mafia'

replacements = [
    ('import com.leafcellteam.mafia.gameRoom.mvi.', 'import com.leafcellteam.shared.gameRoom.mvi.'),
    ('import com.leafcellteam.mafia.gameRoom.roleModelStates.', 'import com.leafcellteam.shared.gameRoom.roleStates.'),
    ('import com.leafcellteam.mafia.gameRoom.models.', 'import com.leafcellteam.shared.gameRoom.models.'),
    ('import com.leafcellteam.mafia.waitingSection.waitingRoomModels.', 'import com.leafcellteam.shared.waitingRoom.models.'),
    ('import com.leafcellteam.mafia.waitingSection.mvi.', 'import com.leafcellteam.shared.waitingRoom.mvi.'),
    ('import com.leafcellteam.mafia.retrofitService.retrofitModel.', 'import com.leafcellteam.shared.network.models.'),
    ('import com.leafcellteam.mafia.joinRoom.models.', 'import com.leafcellteam.shared.joinRoom.'),
    ('import com.leafcellteam.mafia.register.registerMvi.', 'import com.leafcellteam.shared.register.mvi.'),
    ('import com.leafcellteam.mafia.register.data.', 'import com.leafcellteam.shared.register.data.'),
    ('import com.leafcellteam.mafia.tokenManager.TokenProvider', 'import com.leafcellteam.shared.token.TokenProvider'),
    ('import com.leafcellteam.mafia.serializableData.', 'import com.leafcellteam.shared.navigation.'),
    ('import com.leafcellteam.mafia.serializableData.fromScreen.', 'import com.leafcellteam.shared.navigation.fromScreen.'),
    ('import com.leafcellteam.mafia.serializableData.route_interface.', 'import com.leafcellteam.shared.navigation.route.'),
    ('import com.leafcellteam.mafia.roles.Role', 'import com.leafcellteam.shared.roles.Role'),
    ('import com.leafcellteam.mafia.roles.RoleData', 'import com.leafcellteam.shared.roles.RoleData'),
    ('import com.leafcellteam.mafia.sharedPreferences.TokenPreferences', 'import com.leafcellteam.shared.token.TokenStorageImpl'),
    ('import com.leafcellteam.mafia.sharedPreferences.TokenPreference', 'import com.leafcellteam.shared.token.TokenStorageImpl'),
]

for root, _, files in os.walk(app_dir):
    for file in files:
        if file.endswith('.kt'):
            file_path = os.path.join(root, file)
            with open(file_path, 'r', encoding='utf-8') as f:
                content = f.read()
            
            new_content = content
            for old, new in replacements:
                new_content = new_content.replace(old, new)
            
            if new_content != content:
                with open(file_path, 'w', encoding='utf-8') as f:
                    f.write(new_content)
                print(f"Updated {file_path}")

print("Done")
