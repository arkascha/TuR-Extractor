# TuR-Extractor
This is a simple utility that allows to process text messages, extract tokens from those messages which will get inserted into a spreadsheet document by appending them on a given sheet. 
It can be configured to process incoming messages to an email account by using the email application's "filter" feature which allows to pipe incoming messages to get them processed. 

## Author
Christian Reiner                                                                                                                                                                                                      
Web: https://christian-reiner.info                                                                                                                                                                                                      
Contact: github@christian-reiner.info                                                                                                                                                                                                      

## Licence
This software is published under the GNU GPLv3 license. 

This licence defines your rights to use and modify this software as you like with a few but important exception. Please check the details if you are not yet familiar with this type of licence: 
https://choosealicense.com/licenses/gpl-3.0/

## Operation
Two operation modes are offered: 

#### Utility mode: 
The utility is started as a shell command, so via command line or via forking a process. 
One single input message will get processed. The message can be fed into the utility in currently two ways: 
  * piped into the standard input descriptor of the utilty
  * by specifying a local file system path to a message file

#### Daemon mode:
The daemon is started as a shell command or as a system service. 
It will accept multiple messages and process them in a sequencial manner. 
Those messages can be fed as a sequence in one of theses ways
  * piped into the standard input descriptor of the daemon
  * by specifying a local file system path to a file socket (Linux / Unix only)
  * send to the daemons network socket listener

## Usage
`java -jar /path/to/TuR-Extractor <options>`

`<options>`: 
* `-a` | `--action`: (optional) output the performed action
* `-e` | `--echo`: (optional) output the processed values
* `-d` | `--debug`: (optional) log debugging info
* `-i <path>` | `--input=<path>`: (optional) a local file system path to the input message file, "-" selects STDIN
* `-o <path>` | `--output=<path>`: (optional) a local file system path to the target spreadsheet document
* `-s <name>` | `--sheet=<name>`: (optional, defaults to "Contacts") the title of the target document sheet 
* `-t` | `--time`: (optional) output the time of action
* `-v` | `--version`: (optional) output the utilities name and version number

`<path>`: a local file system path (e.g. /var/spool/messages/inbound)

`<name>`: a human readable name, keep in mind that the usual shell escaping and quoting needs to get applied

## Build
The utility is implemented based on the IntelliJ IDEA environment. Project meta files are included, so setting up a new project from a cloned git repository should be straight forward. 
No magic is involved though, so it should be possible to use any environment that provides a java SDK and compiler version 1.8 or up. No specific java flavor is required. 

The build process itself is gradle based. So it allows for using a headless pipeline for creating artifacts and distributions. The current release is based on a github workflow pipeline. 