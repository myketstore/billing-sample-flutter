# myket_iap Example Application

A sample Flutter application demonstrating how to integrate and use the [`myket_iap`](../README.md) plugin for Android in-app purchases.

This project is an adaptation of the classic **Trivial Drive** billing scenario, demonstrating:
- Initializing the Myket billing service with your RSA public key.
- Querying inventory and SKU details on app startup.
- Purchasing consumable items (Gas tank refills).
- Consuming items via `MyketIAP.consume()`.
- Purchasing non-consumable items (Upgrade to Premium).
- Developer payload generation and signature verification.
- Handling billing errors and user cancellations gracefully.

---

## Getting Started

### 1. Prerequisites
- An Android device or emulator with the **Myket app** installed.
- An active Myket developer account with your test SKUs configured.

### 2. Configuration
Open [`lib/main.dart`](lib/main.dart) and configure:
1. **RSA Public Key**: Replace the sample public key in `initIab()` with your app's public key from the Myket Developer Panel.
2. **SKUs**: Adjust `SKU_GAS` and `SKU_PREMIUM` to match your registered products on Myket.

### 3. Run the App
From the `example` directory, run:

```bash
flutter pub get
flutter run
```

For more detailed setup instructions, Gradle configurations, and API reference, refer to the [main plugin README](../README.md).
