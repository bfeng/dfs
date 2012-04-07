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

    query = DataFile.all().filter('f_key =', key)

    data_file = query.get()

    if data_file:
      blob_reader = blobstore.BlobReader(data_file.f_value)
      write_string(self, blob_reader.read())
    else:
      write_boolean(self, False)
