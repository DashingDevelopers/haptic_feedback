# haptic_feedback

A haptic feedback plugin for both iOS and Android.

While it utilizes [standard iOS haptics](https://developer.apple.com/design/human-interface-guidelines/playing-haptics#iOS), it aims to emulate these same haptic patterns on Android for a consistent experience across platforms.

## Getting Started

### 1. Add the dependency

```shell
flutter pub add haptic_feedback
```

### 2. Use the plugin

```dart
const _haptics = Haptics();

final canVibrate = await _haptics.canVibrate;

await _haptics.vibrate(HapticType.success);
await _haptics.vibrate(HapticType.warning);
await _haptics.vibrate(HapticType.error);
await _haptics.vibrate(HapticType.light);
await _haptics.vibrate(HapticType.medium);
await _haptics.vibrate(HapticType.heavy);
await _haptics.vibrate(HapticType.rigid);
await _haptics.vibrate(HapticType.soft);
await _haptics.vibrate(HapticType.selection);
```
