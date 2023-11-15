/*
 * Dispositivos Móveis - IFPE 2023
 * Author: Willian Santos
 * Project: ServSimplesApp
 */
package ifpe.edu.br.servsimples.util;

public class ServerResponseCodeParser {


    private static final String TAG = ServerResponseCodeParser.class.getSimpleName();

    public static String parseToString(String message) {
        int code = 0;
        try {
            code = Integer.parseInt(message);
        } catch (Exception e) {
            if (ServSimplesAppLogger.ISLOGABLE)
                ServSimplesAppLogger.e(TAG, "Error parsing integer");
            code = -1;
        }
        if (message.contains("Failed to connect to")) {
            return "Conexão com o servidor falhou";
        }
        switch (code) {
            case ServSimplesConstants.USER_EXISTS:
                return "Usuário já existe";
            case ServSimplesConstants.USER_NOT_EXISTS:
                return "Usuário não existe";
            case ServSimplesConstants.USER_INFO_NOT_MATCH:
                return "Usuário não confere";
            case ServSimplesConstants.USER_INVALID:
                return "Usuário inválido";
            case ServSimplesConstants.TOKEN_NOT_PRESENT:
                return "Token ausente";
            case ServSimplesConstants.TOKEN_DECRYPT_FAILURE:
                return "Falha de decriptação do token";
            case ServSimplesConstants.USERNAME_INVALID:
                return "Nome de usuário inválido";
            case ServSimplesConstants.PASSWORD_INVALID:
                return "Senha inválida";
            case ServSimplesConstants.TOKEN_EXPIRED:
                return "Token expirado";
            case ServSimplesConstants.FAILURE:
                return "Falha";
            case ServSimplesConstants.USER_NOT_ALLOWED:
                return "Permissão negada para o usuário";
            case ServSimplesConstants.SERVICE_INVALID:
                return "Serviço inválido";
            case ServSimplesConstants.SERVER_ERROR:
                return "Ocorreu um erro no servidor";
            default:
                return "Um erro inesperado ocorreu";
        }
    }
}