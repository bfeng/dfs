from google.appengine.ext import db

class DataFile(db.Model):
  f_key = db.StringProperty()
  f_value = db.TextProperty()

