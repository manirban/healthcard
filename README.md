# Development of a Portable Personal Electronic Health Record Device

Here, the complete source package (Java codes of the CMS and Arduino code of Teensy board) and the database schema
files (HC and central server) are uploaded.

**Prerequisite software**
1. Java SE Development Kit 8u151
2. NetBeans IDE 8.2 (version Java EE)
3. MySQL Community Server 5.6
4. SQLite version 3.21.0
5. Teensyduino version 1.40

**Source Code**
1. "card_managment_software" directory contians source file of CMS.
2. "card_reader" directory contains source file of CR.
3. "central_server" directory contains source file of Server.
4. "database" directory contains schema of HC and Server database. 

** Steps**
1. Install all prerequisite softwares mentioned above.
2. In MySQL create a databse named 'server_user_primarydb' and upload the schema file, server_user_primarydb.sql. 
3. Insert the health-card  into card-reader and connect the reader with PC. (USB serial interface is COM4)
4. In Netbeans, Open project ->  HealthCardMedicalSoftware and CardManagmentServer (Project folders are available in 'card_managment_software' and 'central_server' directory).
5. First, run the project named CardManagmentServer. It is a server applciation runs in background.
6. Next, run the project named HealthCardMedicalSoftware. It will open a GUI of CMS.
7. Access the health-card via CMS with login credential(userid: user, password: 1234)
