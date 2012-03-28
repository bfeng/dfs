import webapp2
from insert import Insert

class MainPage(webapp2.RequestHandler):
  def get(self):
    self.response.headers['Content-Type'] = 'text/json'
    self.response.out.write('{"type":"boolean", "value":"true"}')

app = webapp2.WSGIApplication(
            [
              ('/', MainPage),
              ('/insert', Insert)
            ],
            debug=True
      )
