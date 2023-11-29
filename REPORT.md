# CXX GrooveGalaxy / BombAppetit / BlingBank / MediTrack Project Report

## 1. Introduction

(_Provide a brief overview of your project, including the business scenario and the main components: secure documents, infrastructure, and security challenge._)

(_Include a structural diagram, in UML or other standard notation._)

## 2. Project Development

### 2.1. Secure Document Format

#### 2.1.1. Design

Implementation Design Proposal:

1. Protect Operation:

   - Symmetric Key Generation:
     - Generate a secure symmetric key for encrypting the voucher information.
   - Cipher Voucher:
     - Encrypt the voucher information using the generated symmetric key.
   - Cipher Symmetric Key:
     - Encrypt the symmetric key using the public key of the user, ensuring only the user can decrypt it.
   - Nonce Integration:
     - Include a nonce (combination of timestamp + random number) to prevent replay attacks.
   - Digital Signature:
     - Sign the entire document using the private key of the restaurant owner.

2. Check Operation:

   - Nonce Verification:
     - Check and validate the nonce to prevent replay attacks.
   - Digital Signature Verification:
     - Verify the digital signature using the public key of the restaurant owner to ensure authenticity.
   - Return Status:
     - Return a boolean indicating the success or failure of the digital signature and nonce verification.

3. Unprotect Operation:
   - Symmetric Key Decryption:
     - Decrypt the symmetric key using the user's private key.
   - Voucher Decryption:
     - Decrypt the voucher information using the decrypted symmetric key.
   - Remove Security:
     - Remove all cryptographic protection, leaving the original document intact.

Proposed Flow:

1. Protect:
   - Input: Original document, restaurant owner's private key, user's public key.
   - Output: Protected document.
2. Check:
   - Input: Protected document, restaurant owner's public key.
   - Output: Boolean status indicating authenticity and nonce validity.
3. Unprotect:
   - Input: Protected document, user's private key.
   - Output: Original document.

What the Implementation Protects From:

1. Tampering and Forgery:

   - The digital signature ensures the authenticity of the document. Any tampering attempts will be detected during the check operation.

2. Unauthorized Access to Voucher Information:

   - The symmetric key used to encrypt the voucher is itself encrypted with the user's public key. Only the intended user with the corresponding private key can access the voucher information.

3. Replay Attacks:
   - The inclusion of a nonce, a combination of timestamp and a random number, prevents replay attacks. The check operation verifies the freshness of the nonce to ensure that the document is recent.

Guarantees Provided by the Implementation:

1. Confidentiality:

   - The voucher information is encrypted using a symmetric key, and the symmetric key itself is encrypted with the user's public key. This guarantees that only the intended user can decrypt and access the voucher details.

2. Authenticity:

   - The digital signature ensures the authenticity of the document. The check operation verifies the signature using the restaurant owner's public key, providing confidence in the origin of the document.

3. Integrity:

   - The hashing of the entire document before applying the digital signature ensures that the document has not been altered. Any modification would result in a mismatch during the signature verification.

4. Non-Repudiation:

   - The digital signature, coupled with the use of private and public keys, provides non-repudiation. The restaurant owner cannot deny their association with the signed document.

5. Freshness:
   - The nonce, incorporating a timestamp and a random number, guarantees freshness. This prevents replay attacks by ensuring that the document is recent and hasn't been reused.

Example Data Format:

```json
{
  "restaurantInfo": {
    "owner": "Maria Silva",
    "restaurant": "Dona Maria",
    "address": "Rua da Gl??ria, 22, Lisboa",
    "genre": ["Portuguese", "Traditional"],
    "menu": [
      {
        "itemName": "House Steak",
        "category": "Meat",
        "description": "A succulent sirloin grilled steak.",
        "price": 24.99,
        "currency": "EUR"
      },
      {
        "itemName": "Sardines",
        "category": "Fish",
        "description": "A Portuguese staple, accompanied by potatoes and salad.",
        "price": 21.99,
        "currency": "EUR"
      },
      {
        "itemName": "Mushroom Risotto",
        "category": "Vegetarian",
        "description": "Creamy Arborio rice cooked with assorted mushrooms and Parmesan cheese.",
        "price": 16.99,
        "currency": "EUR"
      }
    ],
    "mealVoucher": {
      "encryptedVoucher": "<...>",
      "encryptedSymmetricKey": "<...>",
      "nonce": "<...>",
      "signature": "<...>"
    }
  }
}
```

(_Outline the design of your custom cryptographic library and the rationale behind your design choices, focusing on how it addresses the specific needs of your chosen business scenario._)

(_Include a complete example of your data format, with the designed protections._)

#### 2.1.2. Implementation

(_Detail the implementation process, including the programming language and cryptographic libraries used._)

(_Include challenges faced and how they were overcome._)

### 2.2. Infrastructure

#### 2.2.1. Network and Machine Setup

(_Provide a brief description of the built infrastructure._)

(_Justify the choice of technologies for each server._)

#### 2.2.2. Server Communication Security

(_Discuss how server communications were secured, including the secure channel solutions implemented and any challenges encountered._)

(_Explain what keys exist at the start and how are they distributed?_)

### 2.3. Security Challenge

#### 2.3.1. Challenge Overview

(_Describe the new requirements introduced in the security challenge and how they impacted your original design._)

#### 2.3.2. Attacker Model

(_Define who is fully trusted, partially trusted, or untrusted._)

(_Define how powerful the attacker is, with capabilities and limitations, i.e., what can he do and what he cannot do_)

#### 2.3.3. Solution Design and Implementation

(_Explain how your team redesigned and extended the solution to meet the security challenge, including key distribution and other security measures._)

(_Identify communication entities and the messages they exchange with a UML sequence or collaboration diagram._)

## 3. Conclusion

(_State the main achievements of your work._)

(_Describe which requirements were satisfied, partially satisfied, or not satisfied; with a brief justification for each one._)

(_Identify possible enhancements in the future._)

(_Offer a concluding statement, emphasizing the value of the project experience._)

## 4. Bibliography

(_Present bibliographic references, with clickable links. Always include at least the authors, title, "where published", and year._)

---

END OF REPORT
