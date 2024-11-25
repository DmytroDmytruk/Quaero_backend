const express = require('express');
const http = require('http');
const { Server } = require('socket.io');

const app = express();
const server = http.createServer(app);
const io = new Server(server);

// Статичні файли
app.use(express.static('public'));

// WebSocket логіка
io.on('connection', (socket) => {
    console.log('User connected:', socket.id);

    socket.on('offer', (data) => {
        socket.to(data.target).emit('offer', { offer: data.offer, sender: socket.id });
    });

    socket.on('answer', (data) => {
        socket.to(data.target).emit('answer', { answer: data.answer });
    });

    socket.on('candidate', (data) => {
        socket.to(data.target).emit('candidate', { candidate: data.candidate });
    });

    socket.on('disconnect', () => {
        console.log('User disconnected:', socket.id);
    });
});

// Запуск сервера
server.listen(3000, () => console.log('Server running on http://localhost:3000'));
