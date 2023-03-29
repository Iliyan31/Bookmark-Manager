# My course project for Modern Java Technologies university course
**This is link to the task problem:** https://github.com/fmi/java-course/blob/master/course-projects/bookmarks.md

## Description
This project represents client-server app where the users can easily create and organize their bookmarks.\
To use the functionalities of the app, the user must create an account and log in.\
<br>
To do that the user must create an account with the following command: `register <username> <password>`.\
And then to log in with the command `login <username> <password>`.

### Here is list of all functionalities that the user can access:
1. Create a new group to store bookmarks with `new-group <group-name>`.
2. Add a new bookmark to a group with `add-to <group-name> <bookmark> {--shorten}`.
 - The `{--shorten}` command shortens the bookmark link with the help of bitly.
3. Remove a bookmark from a group with `remove-from <group-name> <bookmark>`.
4. List all bookmarks with `list`.
5. List all bookmarks from a group with `list --group-name <group-name>`.
6. Search bookmarks by tags with `search --tags <tag> [<tag> ...]`.
7. Search bookmarks by title with `search --title <title>`.
8. Cleaning up invalid links with `cleanup`.
9. Add bookmarks from Chrome with `import-from-chrome`.
10. List all chrome bookmarks with `get-chrome-bookmarks`.

When the user wants to leave the app, they can do it `logout`.

## External libraries
Here is list of all external libraries needed in order to run the project:
### *Java*
- **Java 19** - openjdk version 19.0.1
### *JUnit*
- **JUnit5.8.1** 
### *Google Gson*
- **google.code.gson** version 2.10.1
### *Jsoup*
- **jsoup** version 1.15.3

### *Mockito*
- **byte-buddy** version 1.12.19
- **byte-buddy-agent** version 1.12.19
- **mockito-core** version 4.9.0
- **mockito-junit-jupiter** version 4.9.0
- **objenesis** version 3.3 
