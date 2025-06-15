set openssl="C:\Program Files (x86)\OpenSSL-Win32\bin\openssl.exe"
%openssl% genpkey -algorithm RSA -out alice-private.pem -pass pass:alice -pkeyopt rsa_keygen_bits:2048 -aes256
%openssl% req -x509 -new -key alice-private.pem -passin pass:alice -days 365 -out alice-public.pem -subj "/C=VN/ST=Hanoi/L=CauGiay/O=FUNiX/OU=ITDept/CN=alice@funix.edu.com"

%openssl% genpkey -algorithm RSA -out bob-private.pem -pass pass:bob -pkeyopt rsa_keygen_bits:2048 -aes256
%openssl% req -x509 -new -key bob-private.pem -passin pass:bob -days 365 -out bob-public.pem -subj "/C=VN/ST=Hanoi/L=CauGiay/O=FUNiX/OU=ITDept/CN=bob@funix.edu.com"

%openssl% genpkey -algorithm RSA -out charlie-private.pem -pass pass:charlie -pkeyopt rsa_keygen_bits:2048 -aes256
%openssl% req -x509 -new -key charlie-private.pem -passin pass:charlie -days 365 -out charlie-public.pem -subj "/C=VN/ST=Hanoi/L=CauGiay/O=FUNiX/OU=ITDept/CN=charlie@funix.edu.com"

