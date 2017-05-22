# Nabu

A vocabulary drill tool as used on http://nabu.uzh.ch. It provides

* good interface to drill different aspects of vocabularies
* good interface for teachers to upload new vocabularies
* support for "exotic" languages as Middle Egyptian (Hieroglyphs),
  Accadian (Cuneiform)...
* Java-GUI for offline desktop learning
* Javascript-Version for offline multiple choice learning
* produce PDFs to print cards for learning with those (if you cut them right)
* crossword mode

This was mainly developed in the years 2002-2007.
Parts were funded by the [E-Learning Koordination of the Philosophical faculty of UZH](http://www.phil.uzh.ch/de/fakultaet/dlf.html).
It is somewhat outdated at the moment, but still usable.
Some documentation can be found in [doku] and [docs].

It is licensed under the AGPL.

## Installation

The main part is a Tomcat-Application in `web` with a helper webapp in `webutil`.

> Currently Tomcat has to be configured with by setting
> `org.apache.jasper.compiler.Parser.STRICT_QUOTE_ESCAPING=false`
>  in `$TOMCAT_HOME/conf/catalina.properties`.
> This works only until Tomcat 8.0.* and is not supported on 8.5 anymore.
> 
> Furthermore the WebSockets are currently not working and have to be disabled.

The Docker image is used as:
```bash
docker pull thomann/nabu
docker run -it --rm -p 8080:8080 thomann/nabu
```
and then visit http://localhost:8080/nabu/ (or use the IP on which your docker is running).

If you want to setup a server that already has lots of interesting examples, useThe Docker image is used as:
```bash
docker pull thomann/nabu:latest-examples
docker run -it --rm -p 8080:8080 thomann/nabu:latest-examples
```
and then visit http://localhost:8080/nabu/ (or use the IP on which your docker is running).


## Thanks

Thanks to all of the libraries and fonts we are using, e.g.:

- hiero from LaTeX
- cuneiform from LaTeX

Also we interface the (similarly outdated) projects
[freetts](https://sourceforge.net/projects/freetts/) and
[Mbrola](http://tcts.fpms.ac.be/synthesis/mbrola.html) for text-to-speech.
