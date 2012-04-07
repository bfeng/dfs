import urllib
import webapp2
from google.appengine.ext import blobstore
from models import DataFile
from interface import write_boolean

class Remove(webapp2.RequestHandler):
  def get(self):
    self.post()

  def post(self):
    key = urllib.unquote(self.request.get('key'))

    query = DataFile.all().filter('f_key =', key)
    data_file = query.get()
    if data_file is None:
      write_boolean(self, False)
    else:
      data_file.f_value.delete()
      data_file.delete()
      write_boolean(self, True)
