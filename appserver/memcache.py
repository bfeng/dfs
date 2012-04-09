from google.appengine.api import memcache
import webapp2
from interface import write_boolean

class Memcache(webapp2.RequestHandler):
  def get(self):
    self.post()

  def post(self):
    turn = self.request.get('turn')

    if turn is not None:
      if turn == 'on' or turn == 'off':
        memcache.set(key="turn", value=turn, time=3600)
        write_boolean(self, True)
      else:
        write_boolean(self, False)
    else:
      write_boolean(self, False)
