
class VideoStream:

	def __init__(self, filename):
		self.filename = filename
		try:
			self.file = open(filename, 'rb')
		except:
			raise IOError
		self.frameNum = 0
		
	def next_frame(self):
		#Get next frame.
		data = self.file.read(5) # Get the framelength from the first 5 bits
		if data: 
			framelength = int(data)
							
			# Read the current frame
			data = self.file.read(framelength)
			self.frameNum += 1
			return data
		else:
			# Return None when the file ends
			return None

	def frame_nbr(self):
		#Get frame number.
		return self.frameNum


    