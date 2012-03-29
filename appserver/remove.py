import webapp2
from models import DataFile
from interface import write_boolean

class Remove(webapp2.RequestHandler):
  def get(self):
    self.post()

  def post(self):
    key = self.request.get('key')

    query = DataFile.all().filter('key =', key)
    data_file = query.get()
    if data_file is None:
      write_boolean(False)
    else:
      data_file.delete()
      write_boolean(True)
