import './index.css';
import React from 'react';
import {BrowserRouter, Routes, Route, Link} from "react-router-dom";
import Landing from './components/Landing';
import Manager from './components/Manager';

function App() {
  
  return (
    <BrowserRouter>
      
      <main>
        <Routes>
          <Route exact path = "/" element={<Landing/>}/>
          <Route exact path = "/manager" element={<Manager/>}/>
        </Routes>
      </main>
    </BrowserRouter>
  )
}

export default App;
