import webapp2
from interface import write_boolean
from insert import Insert
from check import Check
from find import Find
from remove import Remove
from listing import Listing
from memcache import Memcache

class MainPage(webapp2.RequestHandler):
  def get(self):
    write_boolean(self, True)

  def post(self):
    self.get()

app = webapp2.WSGIApplication(
            [
              ('/', MainPage),
              ('/insert', Insert),
              ('/check', Check),
              ('/find', Find),
              ('/remove', Remove),
              ('/listing', Listing),
              ('/memcache', Memcache)
            ],
            debug=True)
