# shipper
Shipper is a logstash clone we built (in a hurry) to overcome performance problems with logstash.

# Highlights

Although the software is quite rough on so many edges, we believe that the architecture is sound, especially regarding:

- logstash like configuration 
- interface only plugin architecture
- field evaluation, which is, where possible, computed at pipeline instantiation time
- developer friendly command line tools

This project is and will always be open source: we release it in the public domain hoping for collaboration-

## Performance

We were able to saturate a 7 (speedy) nodes cluster with a single instance of shipper running on a 24 core VM at 50% CPU occupation. Speed gain is around one order of magnitude (probably a bit less)

We're reaching "easily" 150k logging events/sec (on a not-so-trivial http server log analysis system), a number which would have probably required to re-architect the system with load balancers and so on, had we used logstash

## Weak points

- bad support for regexes
- only file input (no rotation support)
- code cleanup is needed
- serious unit testing is badly needed
- plugins are still embedded, although it's not that difficult 

# Building

The software can be built with gradle, with a simple

``` 
 gradle build
``` 

A (versionless) RPM can be built with

``` 
gradle :shipper:rpm
```

# Configuration

The configuration is quite similar to logstash one.

The "entry point" for configuration, at least for the SystemD service, is a file similar to logstash's pipelines.yml 

``` 
- pipeline.id: cdn
  pipeline.batch.size: 512
  path.config: "conf.d/{00-input.conf,10-filter.conf,20-output.conf}"
``` 

Apart quirks / incomplete code / missing plugins the actual pipeline files should follow the same syntax and semantic of logstash configuration files

There are and there will be extensions to the syntax of logstash pipeline configuration files, which in our humble opinion is not expressive enough

# Running

The RPM contains a systemd service which may be started and enabled with:

``` 
systemctl enable shipper
systemctl start shipper
``` 


