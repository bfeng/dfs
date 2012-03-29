import webapp2
from models import DataFile
from interface import write_string
from interface import write_boolean

class Find(webapp2.RequestHandler):
  def get(self):
    self.post()

  def post(self):
    key = self.request.get('key')

    query = DataFile.all().filter('key =', key)

    data_file = query.get()

    if data_file:
      write_string(data_file.value)
    else:
      write_boolean(False)
