from __future__ import with_statement
from google.appengine.api import files
import webapp2
from interface import write_boolean

class Insert(webapp2.RequestHandler):
  def get(self):
    self.post()

  def post(self):
    key = self.request.get('key')
    value = self.request.get('value')

    # Create a file on gs
    filename = '/gs/save-files/' + key
    writable_file_name = files.gs.create(filename, mime_type='application/octect-stream')

    with files.open(writable_file_name, 'a') as f:
      f.write(value)
    files.finalize(writable_file_name)

    write_boolean(self, True)
