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
* `-h` | `--help`: (optional) output the usage message
* `-i <path>` | `--infile=<path>`: (optional) a local file system path of an input message file, "-" selects STDIN
* `-I <pattern>` | `--inpattern=<pattern>`: (optional) a local file system path pattern of input message files
* `-l` | `--log`: (optional) log debugging info
* `-o <path>` | `--outfile=<path>`: (optional) a local file system path of a output spreadsheet document
* `-s <name>` | `--sheet=<name>`: (optional, defaults to "Contacts") the title of the target document sheet 
* `-t` | `--time`: (optional) output the time of action
* `-v` | `--version`: (optional) output the utilities name and version number
* `-?` | `--usage`: (optional) output the usage message

`<path>`: a local file system path (e.g. /var/spool/messages/inbound)

`<pattern>`: a local wildpattern path (e.g. ./work/inbound/accept-*.text)

`<name>`: a human readable name, shell escaped (e.g. "new\ registrations")

## Build
The utility is implemented based on the IntelliJ IDEA environment. Project meta files are included, so setting up a new project from a cloned git repository should be straight forward. 
No magic is involved though, so it should be possible to use any environment that provides a java SDK and compiler version 1.8 or up. No specific java flavor is required. 

The build process itself is gradle based. So it allows for using a headless pipeline for creating artifacts and distributions. The current release is based on a github workflow pipeline.

## Examples

### Processing of incoming email messages
The utility can be configured as a filter to process incoming email messages along with the "filter message" feature modern email applications offer. Here are two of such setups: 

#### scan email messages using kmail2's mighty filter feature :
###### (KDE plasma and akonadi based email client, part of the 'kontact' PIM suite)
The "settings" menu allows to "Configure filters". Here you can define a new filter "TuR-Extractor" which you can defined to get applied to your liking, automatically or manually, on all or just specific messages. 

As a filter action you chose "Execute command" and enter something like `java -jar /path/toTuR-Extractor.jar -aei - -o /path/to/document.xlsm -s "import"`. 

This allows to add one entry per incoming email message to an existing or created spreadsheet document on the existing or created sheet "incoming". 

As a variant to can add logging which allows to track down issues with your setup. 
For that use standard shell features: `java -jar /path/toTuR-Extractor.jar -aei - -o /path/to/document.xlsm -s "import" 1>/path/to/TuR-Extractor.log 2>&1`. 
That way you receive a fresh log of a processed message for each event including useful information like missing folders are file system permission stuff.

#### read from a single local file
###### (allows to specify a single file path on the command line)
A specific file in the local file system can be specified on the command line. That files content will get read, parsed and processed as plain text: 

`java -jar ./path/toTuR-Extractor.jar -aei ./messages/inbound.data -o path/to/document.xlsm -s "import"`. 

#### read from multiple files specified by a pattern 
###### (glob like feature to scan a file system folder)
You can specify a glob like file path pattern to select multiple files in the local file system to get processed. 
A typical scenario for this would be a local spool folder where incoming messages are dropped: 

`java -jar ./path/toTuR-Extractor.jar -aeI ./messages/message-*.text -o path/to/document.xlsm -s "import"`. 
