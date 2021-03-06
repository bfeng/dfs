from __future__ import with_statement
from google.appengine.api import files
from google.appengine.api import memcache
from google.appengine.ext.blobstore import BlobInfo
import urllib
import webapp2
from models import DataFile
from interface import write_boolean

class Insert(webapp2.RequestHandler):
  def get(self):
    self.post()

  def post(self):
    key = self.request.get('key')
    value = self.request.get('value')

    filename = urllib.unquote(key)

    # Clean up current file
    query = DataFile.all().filter("f_key", filename)

    for data_file in query:
      data_file.delete()

    # Create a file
    writable_file_name = files.blobstore.create(mime_type='application/octect-stream')

    with files.open(writable_file_name, 'a') as f:
      f.write(value)
    files.finalize(writable_file_name)

    blob_key = files.blobstore.get_blob_key(writable_file_name)

    data_file = DataFile(f_key = filename, f_value = blob_key)
    data_file.put()

    if memcache.get(key="turn") == 'on':
      if BlobInfo.get(blob_key).size <= 100000:
        memcache.set(key=filename, value=value, time=3600)

    write_boolean(self, True)
