import base64
from Crypto.Cipher import Blowfish

raw = 'pNPyUDezG7cRcc7dmvUTk4AEVCjXL2TVVH5q24lTN//9y65aNVsLUpG2z00L1fa3'
salt = '78075b63-6453-4bc0-88ed-78272197476d'

raw_bin = base64.b64decode(raw)

print raw_bin

key = b'123456'

cipher = Blowfish.new(key)

result = cipher.decrypt(raw_bin)

print result
