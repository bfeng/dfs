import webapp2
from interface import write_json
from models import DataFile

class Listing(webapp2.RequestHandler):
  def get(self):
    self.post()

  def post(self):
    data_files = DataFile.all()

    result = {"type":"array", "value":[]}
    for data_file in data_files:
      result["value"].append(data_file.f_key)

    write_json(self, result)
