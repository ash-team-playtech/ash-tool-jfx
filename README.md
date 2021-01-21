# ASH-Tool (JavaFX)

Helpful set of tools combined into one program which automates routine tasks and improves development efficiency.

Used technologies: Java 14, Spring Boot, JavaFX

## Tools:  
  
### **Font fixer**  

Changes **XOffset, YOffset, XAdvanced** parameters on the specified delta for every char in the config.
#### **Features:**
* support of drag-and-drop for the config file
* auto-detection of config file extension: NGM (*.fnt*), GPAS (*.xml*)
* possibility to override existing config file or save results in a separate one
  
<br/>

### **Images dimensions checker**
  
Checks whether the project contains images that exceed limits:
* mobile - 1024x1024 px
* desktop - 2048x2048 px

Prints out problematic ones.
#### **Features:**
* supports any level of files nesting which allows to run it over multiple projects
* auto-detection of mobile/desktop assets
* excludes temporary folders such as *target, .svn*, etc.
* auto-detection of unique translated assets and check of their equality (takes EN asset as a standard)

