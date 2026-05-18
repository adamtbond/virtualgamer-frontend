import { useEffect, useState } from 'react'

function App() {
  const [notes, setNotes] = useState([])
  const [newNote, setNewNote] = useState('')

  useEffect(() => {
      fetch(`${import.meta.env.VITE_API_BASE_URL}/notes`)
        .then(res => res.json())
        .then(data => setNotes(data))
  }, [])

  const addNote = async () => {
    const response = await fetch(`${import.meta.env.VITE_API_BASE_URL}/notes`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ text: newNote }),
    })

    const savedNote = await response.json()

    setNotes([...notes, savedNote])
    setNewNote('')
  }

  return (
      <div style={{ padding: '40px', fontFamily: 'Arial' }}>
        <h1>Virtual Gamer Notes</h1>

        <input
            value={newNote}
            onChange={(e) => setNewNote(e.target.value)}
            placeholder="Enter note"
        />

        <button onClick={addNote} style={{ marginLeft: '10px' }}>
          Add
        </button>

        <ul>
          {notes.map(note => (
              <li key={note.id}>
                {note.text}
              </li>
          ))}
        </ul>
      </div>
  )
}

export default App