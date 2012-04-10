from google.appengine.api import memcache
from google.appengine.ext import blobstore
import urllib
import webapp2
from models import DataFile
from interface import write_string
from interface import write_boolean

class Find(webapp2.RequestHandler):
  def get(self):
    self.post()

  def post(self):
    key = urllib.unquote(self.request.get('key'))

    if memcache.get(key="turn") == 'on':
      data = memcache.get(key)
      if data is not None:
        write_string(self, data)
        return

    query = DataFile.all().filter('f_key =', key)

    data_file = query.get()

    if data_file:
      blob_reader = blobstore.BlobReader(data_file.f_value, buffer_size=1048576)
      for line in blob_reader:
        write_string(self, line)
    else:
      write_boolean(self, False)
