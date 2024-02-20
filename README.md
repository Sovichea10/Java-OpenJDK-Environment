# Java-OpenJDK-Environment
Java is an object-oriented programming language used mainly for web, desktop, embedded devices and mobile applications. And here is guide about Setup An Envionment.

## 📌Ubuntu

### Update Package in Repo
```
sudo apt-get update
```

### Search available package for OpenJDK
```
sudo apt-cache search openjdk
```

### Install Specific Version - For example, JDK Version 8
```
sudo apt-get install openjdk-8-jdk
```

### List JDK in host
```
dpkg --list | grep -i jdk
```

### Remove or Uninstall package
```
sudo apt remove <package_name>
```

### To upgrade Java JDK version 11
```
sudo apt-get update && sudo apt-get install openjdk-11-jdk
```

### Choose one to be using current version
```
sudo update-alternatives --config java
```
👉 Then select version by number
