from google.appengine.ext import db
from google.appengine.ext import blobstore

class DataFile(db.Model):
  f_key = db.StringProperty()
  f_value = blobstore.BlobReferenceProperty()

