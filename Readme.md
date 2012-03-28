Distributed file system model based on Google App Engine

+------------+    JSON     +--------+
| JavaClient | <=========> | server |
+------------+             +--------+
                           |  GAE   |
                           +--------+

appserver
=========
  This folder contains the source code on Google App Engine.

JavaClient
==========
  This folder holds the source code of the client which communicates with the server hosted on Google App Engine.
