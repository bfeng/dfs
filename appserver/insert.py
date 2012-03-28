import webapp2
from models import DataFile

class Insert(webapp2.RequestHandler):
  def get(self):
    self.post()

  def post(self):
    key = self.request.get('key')
    value = self.request.get('value')

    data_file = DataFile(f_key=key, f_value=value)
    data_file.put()
    self.response.headers['Content-Type'] = 'text/json'
    self.response.out.write('{"type":"boolean", "value":"true"}')
