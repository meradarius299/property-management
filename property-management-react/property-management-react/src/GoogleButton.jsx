import { useEffect, useRef } from 'react'
import { GOOGLE_CLIENT_ID } from './config.js'

export default function GoogleButton({ role, onCredential }) {
    const buttonRef = useRef(null);

    useEffect(() => {
        if (!window.google || !buttonRef.current) return;

        window.google.accounts.id.initialize({
            client_id: GOOGLE_CLIENT_ID,
            callback: (response) => onCredential(response.credential, role),
        });

        window.google.accounts.id.renderButton(buttonRef.current, {
            theme: 'outline',
            size: 'large',
            width: 320,
            text: 'continue_with',
        });
    }, [role, onCredential]);

    return <div ref={buttonRef} className="google-btn-slot"></div>;
}