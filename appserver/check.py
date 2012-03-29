import webapp2
from models import DataFile
from interface import write_boolean

class Check(webapp2.RequestHandler):
  def get(self):
    self.post()

  def post(self):
    key = self.request.get('key')

    query = DataFile.gql("WHERE key = :1", key)
    if query.count() >= 1:
      write_boolean(self, True)
    else:
      write_boolean(self, False)
