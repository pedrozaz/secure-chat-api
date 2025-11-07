# Secure Chat API Example
This is a simple Spring Boot project demonstrating how to establish a secure communication channel between a client and
a server using the Diffie-Hellman key exchange and AES/GCM for symmetric encryption.

## Core Concepts
- **Key Exchange**: Uses Diffie-Hellman (DH) to allow a client and server to agree on a shared secret over an insecure channel.
- **Symmetric Encryption**: Uses AES/GCM to encrypt and decrypt messages using the shared secret.
- **Key Derivation**: Uses SHA-256 as a Key Derivation Function (KDF) to generate a fixed-size AES key from the DH shared secret.
- **Persistence**: Uses Spring Data JPA and PostgreSQL to store session state (ID and shared secret).

## How It Works
The communication flow is divided into two main phases:

1. **Handshake**: the client initiates a handshake by sending its public DH key. The server generates its own key pair, computes the shared secret,
   saves the session to the database, and returns its public key and unique sessionID.
2. **Secure Messaging**: the client uses the shared secret to encrypt a message sand sends it to the server along with the session ID.
   The server retrives the corresponding secret from the database, decrypts the message, and confirms receipt.

## API Endpoints

### 1. Start Handshake

> POST /api/handshake/start
> 
- **Request Body**:
```
{
    "publicKey": "<base64_encoded_client_public_key>"
}
```


- **Success Response**:
```
{
    "sessionId": "<unique_session_id>",
    "serverPublicKey": "<base64_encoded_server_public_key>"
}
```
#
- **Example**:
<img width="973" height="654" alt="image" src="https://github.com/user-attachments/assets/3837ae9d-5b9e-402c-9cb8-e3087aee5f79" />


#
#
### 2. Send Secure Message

> POST /api/chat/message
- **Request Body**:
```
{
    "sessionId": "<session_id_from_handshake>",
    "iv": "<base64_encoded_initialization_vector>",
    "ciphertext": "<base64_encoded_encrypted_message>"
}
```

- **Success Response**:
```
{
    "status": "Message received and decrypted successfully",
    "decryptedData": "<original_plaintext_message>"
}
```
#
- **Example**:
<img width="973" height="549" alt="image" src="https://github.com/user-attachments/assets/46f6dad6-a46d-4ce1-823e-084fcd8bb3db" />

