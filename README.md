# A37 BombAppetit

## Team

| Number | Name               | User                               | E-mail                                 |
| ------ | ------------------ | ---------------------------------- | -------------------------------------- |
| 99194  | Daniel Pereira     | <https://github.com/DaniPalma2002> | <mailto:alice@tecnico.ulisboa.pt>      |
| 99315  | Ricardo Toscanelli | <https://github.com/rtoscanelli>   | <mailto:bob@tecnico.ulisboa.pt>        |
| 99328  | Simão Gato         | <https://github.com/SimaoGato>     | <mailto:simao.gato@tecnico.ulisboa.pt> |

![Alice](img/alice.png) ![Bob](img/bob.png) ![Charlie](img/charlie.png)

_(add face photos with 150px height; faces should have similar size and framing)_

## Contents

This repository contains documentation and source code for the _Network and Computer Security (SIRS)_ project.

The [REPORT](REPORT.md) document provides a detailed overview of the key technical decisions and various components of the implemented project.
It offers insights into the rationale behind these choices, the project's architecture, and the impact of these decisions on the overall functionality and performance of the system.

This document presents installation and demonstration instructions.

_(adapt all of the following to your project, changing to the specific Linux distributions, programming languages, libraries, etc)_

## Installation

To see the project in action, it is necessary to setup a virtual environment, with N networks and M machines.

The following diagram shows the networks and machines:

_(include a text-based or an image-based diagram)_

### Prerequisites

All the virtual machines are based on: Linux 64-bit, Kali 2023.3

[Download](https://...link_to_download_installation_media) and [install](https://...link_to_installation_instructions) a virtual machine of Kali Linux 2023.3.  
Clone the base machine to create the other machines.

_(above, replace witch actual links)_

### Machine configurations

For each machine, there is an initialization script with the machine name, with prefix `init-` and suffix `.sh`, that installs all the necessary packages and makes all required configurations in the a clean machine.

Inside each machine, use Git to obtain a copy of all the scripts and code.

```sh
$ git clone https://github.com/tecnico-sec/cxx...
```

_(above, replace with link to actual repository)_

Next we have custom instructions for each machine.

#### Machine 1

This machine runs ...

_(describe what kind of software runs on this machine, e.g. a database server (PostgreSQL 16.1))_

To verify:

```sh
$ setup command
```

_(replace with actual commands)_

To test:

```sh
$ test command
```

_(replace with actual commands)_

The expected results are ...

_(explain what is supposed to happen if all goes well)_

If you receive the following message ... then ...

_(explain how to fix some known problem)_

#### Machine ...

_(similar content structure as Machine 1)_

## Demonstration

Now that all the networks and machines are up and running, ...

_(give a tour of the best features of the application; add screenshots when relevant)_

```sh
$ demo command
```

_(replace with actual commands)_

_(IMPORTANT: show evidence of the security mechanisms in action; show message payloads, print relevant messages, perform simulated attacks to show the defenses in action, etc.)_

This concludes the demonstration.

## Additional Information

### Links to Used Tools and Libraries

- [Java 11.0.16.1](https://openjdk.java.net/)
- [Maven 3.9.5](https://maven.apache.org/)
- ...

### Versioning

We use [SemVer](http://semver.org/) for versioning.

### License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) for details.

_(switch to another license, or no license, as you see fit)_

---

END OF README
