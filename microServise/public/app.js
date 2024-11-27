const socket = io();

const localVideo = document.getElementById('localVideo');
const remoteVideo = document.getElementById('remoteVideo');
const callButton = document.getElementById('callButton');
const endCallButton = document.getElementById('endCallButton');
const usernameInput = document.getElementById('usernameInput');
const callNotification = document.getElementById('callNotification');
const acceptCallButton = document.getElementById('acceptCall');
const rejectCallButton = document.getElementById('rejectCall');
const toggleCameraButton = document.getElementById('toggleCameraButton');
const toggleMicButton = document.getElementById('toggleMicButton'); // Нова кнопка

let localStream;
let peerConnection;
let currentCaller = null;
let cameraEnabled = true;
let micEnabled = true; // Стан мікрофона

// STUN server configuration
const config = {
    iceServers: [
        {
            urls: 'stun:stun.l.google.com:19302',
        },
    ],
};

// Get local video stream
navigator.mediaDevices
    .getUserMedia({ video: true, audio: true })
    .then((stream) => {
        localStream = stream;
        localVideo.srcObject = stream;
    })
    .catch((error) => {
        console.error('Error accessing media devices.', error);
    });

// Call user
callButton.addEventListener('click', () => {
    const target = usernameInput.value;
    if (!target) return alert('Please enter a user ID to call.');

    socket.emit('call-user', { target });
});

// Handle incoming call request
socket.on('call-request', (data) => {
    callNotification.style.display = 'block';
    currentCaller = data.from;
});

// Accept call
acceptCallButton.addEventListener('click', () => {
    callNotification.style.display = 'none';

    peerConnection = new RTCPeerConnection(config);
    localStream.getTracks().forEach((track) => peerConnection.addTrack(track, localStream));

    peerConnection.ontrack = (event) => {
        remoteVideo.srcObject = event.streams[0];
    };

    peerConnection.onicecandidate = (event) => {
        if (event.candidate) {
            socket.emit('ice-candidate', {
                target: currentCaller,
                candidate: event.candidate,
            });
        }
    };

    peerConnection
        .createOffer()
        .then((offer) => {
            peerConnection.setLocalDescription(offer);
            socket.emit('send-offer', { target: currentCaller, offer });
        })
        .catch((error) => console.error('Error creating offer.', error));
});

// Reject call
rejectCallButton.addEventListener('click', () => {
    callNotification.style.display = 'none';
    currentCaller = null;
});

// Toggle camera
toggleCameraButton.addEventListener('click', () => {
    if (!localStream) return;

    const videoTrack = localStream.getVideoTracks()[0];
    if (videoTrack) {
        cameraEnabled = !cameraEnabled;
        videoTrack.enabled = cameraEnabled;
        toggleCameraButton.textContent = cameraEnabled ? 'Turn Camera Off' : 'Turn Camera On';
    }
});

// Toggle microphone
toggleMicButton.addEventListener('click', () => {
    if (!localStream) return;

    const audioTrack = localStream.getAudioTracks()[0];
    if (audioTrack) {
        micEnabled = !micEnabled;
        audioTrack.enabled = micEnabled;
        toggleMicButton.textContent = micEnabled ? 'Turn Microphone Off' : 'Turn Microphone On';
    }
});

// End call
endCallButton.addEventListener('click', () => {
    if (peerConnection) {
        peerConnection.close();
        peerConnection = null;
    }

    // Очищення відео
    remoteVideo.srcObject = null;

    // Повідомлення іншого користувача про завершення дзвінка
    socket.emit('end-call', { target: currentCaller });
    currentCaller = null;
    
});
socket.on('call-ended', () => {
    if (peerConnection) {
        peerConnection.close();
        peerConnection = null;
    }

    // Очищення відео
    remoteVideo.srcObject = null;

    alert('The call has been ended by the other user.');
});

// Handle incoming offer
socket.on('offer-received', (data) => {
    peerConnection = new RTCPeerConnection(config);

    peerConnection.ontrack = (event) => {
        remoteVideo.srcObject = event.streams[0];
    };

    peerConnection.onicecandidate = (event) => {
        if (event.candidate) {
            socket.emit('ice-candidate', {
                target: data.from,
                candidate: event.candidate,
            });
        }
    };

    peerConnection
        .setRemoteDescription(new RTCSessionDescription(data.offer))
        .then(() => {
            localStream.getTracks().forEach((track) =>
                peerConnection.addTrack(track, localStream)
            );

            return peerConnection.createAnswer();
        })
        .then((answer) => {
            peerConnection.setLocalDescription(answer);
            socket.emit('send-answer', { target: data.from, answer });
        })
        .catch((error) => console.error('Error handling offer.', error));
});

// Handle incoming answer
socket.on('answer-received', (data) => {
    peerConnection.setRemoteDescription(new RTCSessionDescription(data.answer));
});

// Handle incoming ICE candidates
socket.on('ice-candidate', (data) => {
    if (peerConnection) {
        peerConnection.addIceCandidate(new RTCIceCandidate(data.candidate)).catch((error) =>
            console.error('Error adding received ICE candidate.', error)
        );
    }
});