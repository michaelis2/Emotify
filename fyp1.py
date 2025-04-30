import cv2
import numpy as np
from tensorflow.keras.models import model_from_json
from tensorflow.keras.preprocessing import image
from flask import Flask, request, jsonify
from werkzeug.utils import secure_filename
import os

# Initialize Flask app
app = Flask(__name__)

# Load the model
model = model_from_json(open("fer.json", "r").read())  # Load model
model.load_weights('fer.h5')  # Load weights

# Load the face detection classifier
face_haar_cascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')

# Define upload folder and allowed extensions
UPLOAD_FOLDER = 'uploads'
ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg'}
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

# Function to check if file has allowed extension
def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS

# Function to predict emotion from the uploaded image
def predict_emotion(img_path):
    img = cv2.imread(img_path)

    if img is None:
        return "Error: Image not found"
    
    gray_img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    faces_detected = face_haar_cascade.detectMultiScale(gray_img, 1.32, 5)

    if len(faces_detected) == 0:
        return "Error: No faces detected"

    for (x, y, w, h) in faces_detected:
        roi_gray = gray_img[y:y + w, x:x + h]
        roi_gray = cv2.resize(roi_gray, (48, 48))

        img_pixels = image.img_to_array(roi_gray)
        img_pixels = np.expand_dims(img_pixels, axis=0)
        img_pixels /= 255

        predictions = model.predict(img_pixels)
        max_index = np.argmax(predictions[0])  # Find max indexed array
        emotions = ['Angry', 'Disgust', 'Fear', 'Happy', 'Sad', 'Surprise', 'Neutral']
        predicted_emotion = emotions[max_index]

        print(f"Predicted Emotion: {predicted_emotion}")
        
        return predicted_emotion



# Route for uploading image and predicting emotion
@app.route('/predict', methods=['POST'])
def upload_image():
    if 'file' not in request.files:
        return jsonify({"error": "No file part"}), 400
    file = request.files['file']
    if file.filename == '':
        return jsonify({"error": "No selected file"}), 400

    if file and allowed_file(file.filename):
        filename = secure_filename(file.filename)
        file_path = os.path.join(app.config['UPLOAD_FOLDER'], filename)
        file.save(file_path)

        emotion = predict_emotion(file_path)
        return jsonify({"emotion": emotion})

    return jsonify({"error": "Invalid file format"}), 400

# Run the Flask app
if __name__ == '__main__':
    if not os.path.exists(UPLOAD_FOLDER):
        os.makedirs(UPLOAD_FOLDER)
    app.run(debug=True)
