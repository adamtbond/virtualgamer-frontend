import { useEffect, useState } from 'react'

const API = import.meta.env.VITE_API_BASE_URL

function App() {
    const [notes, setNotes] = useState([])
    const [newNote, setNewNote] = useState('')
    const [username, setUsername] = useState('adam')
    const [password, setPassword] = useState('test123')
    const [token, setToken] = useState(localStorage.getItem('token') || '')

    const authHeaders = {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
    }

    const loadNotes = async () => {
        if (!token) return

        const res = await fetch(`${API}/notes`, {
            headers: authHeaders,
        })

        if (res.ok) {
            const data = await res.json()
            setNotes(data)
        }
    }

    useEffect(() => {
        loadNotes()
    }, [token])

    const register = async () => {
        await fetch(`${API}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password }),
        })

        alert('Registered')
    }

    const login = async () => {
        const res = await fetch(`${API}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password }),
        })

        const data = await res.json()
        localStorage.setItem('token', data.token)
        setToken(data.token)
    }

    const logout = () => {
        localStorage.removeItem('token')
        setToken('')
        setNotes([])
    }

    const addNote = async () => {
        const res = await fetch(`${API}/notes`, {
            method: 'POST',
            headers: authHeaders,
            body: JSON.stringify({ text: newNote }),
        })

        if (res.ok) {
            const savedNote = await res.json()
            setNotes([...notes, savedNote])
            setNewNote('')
        }
    }

    return (
        <div style={{ padding: '40px', fontFamily: 'Arial' }}>
            <h1>Virtual Gamer Notes</h1>

            {!token ? (
                <>
                    <h2>Login / Register</h2>

                    <input
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        placeholder="Username"
                    />

                    <input
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        placeholder="Password"
                        type="password"
                        style={{ marginLeft: '10px' }}
                    />

                    <button onClick={register} style={{ marginLeft: '10px' }}>
                        Register
                    </button>

                    <button onClick={login} style={{ marginLeft: '10px' }}>
                        Login
                    </button>
                </>
            ) : (
                <>
                    <p>Logged in as {username}</p>

                    <button onClick={logout}>Logout</button>

                    <h2>Notes</h2>

                    <input
                        value={newNote}
                        onChange={(e) => setNewNote(e.target.value)}
                        placeholder="Enter note"
                    />

                    <button onClick={addNote} style={{ marginLeft: '10px' }}>
                        Add
                    </button>

                    <ul>
                        {notes.map((note) => (
                            <li key={note.id}>{note.text}</li>
                        ))}
                    </ul>
                </>
            )}
        </div>
    )
}

export default App