from __future__ import with_statement
from google.appengine.api import files
import webapp2
from interface import write_string
from interface import write_boolean

class Find(webapp2.RequestHandler):
  def get(self):
    self.post()

  def post(self):
    key = self.request.get('key')

    filename = '/gs/save-files/' + key

    with files.open(filename, 'r') as f:
      value = ""
      data = f.read(64)
      value = data
      while data != "":
        data = f.read(64)
        value = value + data
      write_string(self, value)
      return

    write_boolean(self, False)
