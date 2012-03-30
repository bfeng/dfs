from __future__ import with_statement
from google.appengine.api import files
import webapp2
from interface import write_boolean

class Remove(webapp2.RequestHandler):
  def get(self):
    self.post()

  def post(self):
    key = self.request.get('key')

    filename = '/gs/save-files/' + key

    try:
      with files.open(filename, 'r') as f:
        write_boolean(self, True)
    except files.ExistenceError:
      write_boolean(self, False)
