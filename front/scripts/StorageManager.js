class StorageManager {
    constructor() {
        this.encryptionKey = this.generateOrGetKey();
    }

    generateOrGetKey() {
        let key = localStorage.getItem('encryption_key');
        if (!key) {
            key = window.crypto.getRandomValues(new Uint32Array(8)).join('');
            localStorage.setItem('encryption_key', key);
        }
        return key;
    }

    async encrypt(text) {
        const encoder = new TextEncoder();
        const data = encoder.encode(text);
        
        const key = await crypto.subtle.importKey(
            'raw',
            encoder.encode(this.encryptionKey),
            { name: 'AES-CBC' },
            false,
            ['encrypt']
        );
        
        const iv = crypto.getRandomValues(new Uint8Array(16));
        const encrypted = await crypto.subtle.encrypt(
            { name: 'AES-CBC', iv },
            key,
            data
        );
        
        return {
            iv: Array.from(iv),
            data: Array.from(new Uint8Array(encrypted))
        };
    }

    async decrypt(encryptedData) {
        const decoder = new TextDecoder();
        const encoder = new TextEncoder();
        
        const key = await crypto.subtle.importKey(
            'raw',
            encoder.encode(this.encryptionKey),
            { name: 'AES-CBC' },
            false,
            ['decrypt']
        );
        
        const decrypted = await crypto.subtle.decrypt(
            { name: 'AES-CBC', iv: new Uint8Array(encryptedData.iv) },
            key,
            new Uint8Array(encryptedData.data)
        );
        
        return decoder.decode(decrypted);
    }

    async saveToken(token) {
        try {
            const encrypted = await this.encrypt(token);
            localStorage.setItem('auth_token', JSON.stringify(encrypted));
            return true;
        } catch (error) {
            console.error('Error saving token:', error);
            return false;
        }
    }

    async getToken() {
        try {
            const encrypted = localStorage.getItem('auth_token');
            if (!encrypted) return null;
            
            const encryptedData = JSON.parse(encrypted);
            return await this.decrypt(encryptedData);
        } catch (error) {
            console.error('Error getting token:', error);
            return null;
        }
    }

    clearToken() {
        localStorage.removeItem('auth_token');
    }
}