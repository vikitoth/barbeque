# Barbeque weather app
### Collection of the application improvements

- Application testing
  - finish and fixing android testing part
    - create different json files to Mockserver
    - location testing

- Developed application
  - it needs to clarify the Business logic
     namely on which time interval to look at the weather data - all day or 3 hour slot
  - app is refreshed if the location is changed
  - adding launcher icon
  - Text display is depend on location

### Suggestion further application testing

- no gps / internet connection
- maybe different mobile connection( 5G, LTE, ...)
- how to work app if battery is low
- accessibility test

### Installation and execution

1. Clone the project or import to your favourite IDE as Project from Version control
   - `git clone https://github.com/vikitoth/barbeque.git`
2. Create emulator eg. Pixel 3a API30
3. Run application with default configuration or use CLI:
   - `adb shell am start -n "com.viki.toth.barbequeTime/com.viki.toth.barbequeTime.MainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHE`