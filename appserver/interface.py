import json

def write_json(re, jsonobj):
  re.response.headers['Content-type'] = 'text/javascript'
  re.response.out.write(json.dumps(jsonobj))

def write_boolean(re, result):
  json = {'type':'boolean', 'value':result}
  write_json(re, json)

def write_string(re, result):
  json = {'type':'string', 'value':result}
  write_json(re, json)
