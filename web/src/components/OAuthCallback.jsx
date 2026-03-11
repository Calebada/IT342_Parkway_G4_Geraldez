import React, { useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';

function OAuthCallback() {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();

    useEffect(() => {
        const userId = searchParams.get('userId');
        const email = searchParams.get('email');
        const firstname = searchParams.get('firstname');
        const lastname = searchParams.get('lastname');
        const role = searchParams.get('role');
        const error = searchParams.get('error');

        if (error) {
            console.error('OAuth login error:', error);
            navigate('/');
            return;
        }

        if (userId && email) {
            // Create user object and save to localStorage
            const userObj = {
                id: parseInt(userId),
                email: email,
                firstname: firstname || '',
                lastname: lastname || '',
                role: role || 'user'
            };
            
            console.log('OAuth login successful:', userObj);
            localStorage.setItem('currentUser', JSON.stringify(userObj));
            
            // Redirect based on role
            if (role === 'admin') {
                navigate('/admin');
            } else {
                navigate('/dashboard');
            }
        } else {
            // No user data in URL, redirect to login
            navigate('/');
        }
    }, [searchParams, navigate]);

    return (
        <div style={{ 
            display: 'flex', 
            justifyContent: 'center', 
            alignItems: 'center', 
            height: '100vh',
            fontSize: '18px'
        }}>
            Logging you in...
        </div>
    );
}

export default OAuthCallback;
