const express = require('express');
const http = require('http');
const bodyParser = require('body-parser');
const { Server } = require('socket.io');
const vacancyCreating = require('./vacancyCreating')
const app = express();
const server = http.createServer(app);
app.use(express.static('public'));
app.use(vacancyCreating);
app.use(bodyParser.json());
const io = new Server(server, {
    cors: {
        origin: '*',
        methods: ['GET', 'POST', 'DELETE', "PUT", "PATCH"],
    },
});

io.on('connection', (socket) => {
    console.log('User connected:', socket.id);

    socket.on('call-user', (data) => {
        console.log(`Call request from ${socket.id} to ${data.target}`);
        io.to(data.target).emit('call-request', {
            from: socket.id,
        });
    });

    socket.on('call-accepted', (data) => {
        console.log(`Call accepted by ${socket.id}`);
        io.to(data.from).emit('call-accepted', {
            answer: data.answer,
        });
    });

    socket.on('ice-candidate', (data) => {
        io.to(data.target).emit('ice-candidate', {
            candidate: data.candidate,
        });
    });

    socket.on('send-offer', (data) => {
        io.to(data.target).emit('offer-received', {
            offer: data.offer,
            from: socket.id,
        });
    });

    socket.on('send-answer', (data) => {
        io.to(data.target).emit('answer-received', {
            answer: data.answer,
        });
    });

    socket.on('disconnect', () => {
        console.log('User disconnected:', socket.id);
    });
    
    socket.on('end-call', (data) => {
        io.to(data.target).emit('call-ended');
    });

    socket.on('disconnect', () => {
        console.log('User disconnected:', socket.id);
    });
});

server.listen(3030, () => console.log('Server running on http://localhost:3030'));