# Free Fire Client Floating Panel

A professional Android floating panel UI for Free Fire client with ES, AIM, and FL toggles.

## Features

✅ **Floating Window** - Panel floats above all apps  
✅ **Draggable** - Drag to reposition anywhere on screen  
✅ **Dark Theme** - Professional dark UI design  
✅ **Toggle Switches** - ES, AIM, FL feature toggles  
✅ **Status Indicator** - Real-time status updates  
✅ **Close Button** - Easy panel dismissal  
✅ **Android 6.0+** - Full compatibility  

## Permissions Required

- `SYSTEM_ALERT_WINDOW` - For overlay functionality
- `INTERNET` - For network operations

## Installation

1. Clone the repository
2. Open in Android Studio
3. Build and run on Android 6.0+ device
4. Grant overlay permission when prompted
5. Click "Launch Floating Panel" button

## Usage

### Launch Panel
```
Button launchPanelBtn = findViewById(R.id.launchPanelBtn);
launchPanelBtn.setOnClickListener(v -> startFloatingPanel());
```

### Close Panel
```
Button closePanelBtn = findViewById(R.id.closePanelBtn);
closePanelBtn.setOnClickListener(v -> stopFloatingPanel());
```

## Project Structure

```
├── AndroidManifest.xml          # App manifest with permissions
├── res/
│   └── layout/
│       ├── activity_main.xml    # Main activity layout
│       └── floating_panel.xml   # Floating panel layout
├── src/
│   ├── MainActivity.java        # Main activity
│   └── FloatingPanelService.java # Floating panel service
├── build.gradle                 # Build configuration
└── README.md                    # This file
```

## Key Components

### MainActivity.java
- Handles permission requests
- Controls service start/stop
- Shows toast notifications

### FloatingPanelService.java
- Manages floating window display
- Handles drag and touch events
- Manages switch toggles
- Updates status text in real-time

## Design

- **Color Scheme**: Dark theme (#0F0F12, #1E1E23)
- **Status Color**: Green (#65D88A)
- **Text Colors**: White (#FFFFFF), Gray (#8E8E96)
- **Accent Color**: Red (#FF6B6B)
- **Panel Size**: 340dp width, wrap_content height

## Notes

- Panel position is saved during drag
- Switches are UI-only toggles (backend integration required)
- Status updates dynamically based on switch states
- Foreground service ensures panel persists

## License

MIT License - Free to use and modify

## Author

Created with ❤️ for Free Fire enthusiasts
