import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { BsBusFrontFill } from 'react-icons/bs';
import '../../styles/LoginRegister.css';

function Login() {
    const navigate = useNavigate();
    const { login } = useAuth();
    const [formData, setFormData] = useState({
        email: '',
        password: ''
    });
    const [error, setError] = useState('');

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        
        const result = await login(formData.email, formData.password);
        
        if (result.success) {
            if (result.user.role === 'admin') {
                navigate('/admin');
            } else {
                navigate('/dashboard');
            }
        } else {
            setError(result.message);
        }
    };

    return (
        <div className="login-container">
            <form onSubmit={handleSubmit}>
                <div className="logo-login">
                    <BsBusFrontFill className="logo-icon" />
                    <h1>&nbsp;ParkWay</h1>
                </div>

                {error && <div className="error-message" style={{ color: '#ff4444', marginBottom: '1rem' }}>{error}</div>}

                <div className="input-box">
                    <input 
                        type="email" 
                        placeholder="Email" 
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        required 
                    />
                    <i className="bx bx-user"></i>
                </div>

                <div className="input-box">
                    <input 
                        type="password" 
                        placeholder="Password" 
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        required 
                    />
                    <i className="bx bx-lock"></i>
                </div>

                <button type="submit" className="login_btn">
                    Login
                </button>

                {/* Google Login Button */}
                <div style={{ marginTop: '1rem', textAlign: 'center' }}>
                    <a href="http://localhost:8080/oauth2/authorization/google" style={{ textDecoration: 'none' }}>
                        <button
                            type="button"
                            style={{
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                gap: '10px',
                                width: '100%',
                                padding: '12px 20px',
                                backgroundColor: '#fff',
                                color: '#757575',
                                border: '1px solid #ddd',
                                borderRadius: '5px',
                                cursor: 'pointer',
                                fontSize: '14px',
                                fontWeight: '500',
                            }}
                        >
                            <img 
                                src="https://www.gstatic.com/firebasejs/ui/2.0.0/images/auth/google.svg" 
                                alt="Google" 
                                style={{ width: '18px', height: '18px' }}
                            />
                            Continue with Google
                        </button>
                    </a>
                </div>

                <div className="register-link">
                    <p>
                        Don't have an account? <Link to="/register">Register</Link>
                    </p>
                </div>
            </form>
        </div>
    );
}

export default Login;