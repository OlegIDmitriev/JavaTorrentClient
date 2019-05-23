package ru.raiffeisen.model.net.http;

public class URLEnc {

    private String original;
    private String encoded;
    private final char[] listAllowed =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.-_~".toCharArray(); // liste des
    // caractères autorisés dans une URL

    public URLEnc(String aEcnoder){
        this.original = aEcnoder;
        converterHash(this.original);

    }

    // on converti un caractère hexa en caractère autorisé dans l'url
    private String converterChar(String toConvert){
        String str = toConvert.substring(0, 2);
        char aTester = (char)Integer.parseInt(toConvert, 16);
        //moche mais fonctionne : on vérifie si le caractère à convertir est dans la liste de car autorisés
        for(char e : listAllowed){
            if(aTester == e){
                return(String.valueOf(e));
            }
        }
        String converted = "%" + toConvert;
        return(converted);
    }

    private void converterHash(String hash){
        char[] c_hash = hash.toCharArray();
        String finalString = "";
        for(int i = 0; i < hash.length() / 2; i++){
            String tmp = new StringBuilder().append(c_hash[i*2]).append(c_hash[i*2 + 1]).toString();
            finalString+=(this.converterChar(tmp));
        }
        this.encoded = finalString;
    }

    public String getEncoded() {
        return this.encoded;
    }
}
