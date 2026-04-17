# GWACalc

GWACalc is a cross-platform **Java Swing desktop application** for computing the **General Weighted Average (GWA)** of subjects based on their **units** and **grades**. It is designed to run on both **Windows** and **macOS**, with a clean desktop UI and a built-in **dark mode** option.

## Features

- Add subject entries with:
  - Subject name
  - Number of units
  - Grade
- Automatically computes **weighted points**
- Calculates overall **GWA**
- Remove selected subject entries
- Clear all entries
- Toggle **Dark Mode**
- Desktop-friendly interface for **Windows** and **macOS**
- Can be packaged as:
  - **`.exe`** for Windows
  - **`.dmg`** for macOS

## Formula Used

GWACalc computes the General Weighted Average using the standard weighted average formula:

```math
\text{GWA} = \frac{\sum (\text{Units} \times \text{Grade})}{\sum \text{Units}}
````

## Technologies Used

* **Java**
* **Java Swing**
* **JPackage** for native packaging

## Project Structure

```text
GWACalc/
├── GWACalc.java
├── dist/
├── output/
├── LICENSE
└── README.md
```

## How to Run

Make sure you have **Java JDK** installed.

### Compile

```bash
javac GWACalc.java
```

### Run

```bash
java GWACalc
```

## How to Build a Runnable JAR

```bash
javac GWACalc.java
jar cfe dist/GWACalc.jar GWACalc *.class
java -jar dist/GWACalc.jar
```

## Packaging the Application

### Windows (`.exe`)

Run this in **Command Prompt** on Windows:

```bat
jpackage --input dist --name GWACalc --main-jar GWACalc.jar --main-class GWACalc --type exe --app-version 1.0 --vendor "Masaharu Kayama" --win-shortcut --win-menu --dest output --verbose
```

### macOS (`.dmg`)

Run this in **Terminal** on macOS:

```bash
jpackage --input dist --name GWACalc --main-jar GWACalc.jar --main-class GWACalc --type dmg --app-version 1.0 --vendor "Masaharu Kayama" --dest output
```

## Notes

* The Windows installer build may require additional Windows packaging tools depending on your environment.
* The application should be built on the same platform as the target installer:

  * build `.exe` on Windows
  * build `.dmg` on macOS

## Screenshots

You can add screenshots here later.

Example:

```md
![GWACalc Light Mode](images/light-mode.png)
![GWACalc Dark Mode](images/dark-mode.png)
```

## Future Improvements

Possible improvements for future versions:

* Edit existing subject entries
* Save and load subject lists
* Export results
* Auto-detect system dark mode
* GPA/GWA conversion presets
* Improved modern UI styling
