package net.herranzmartin.project977r;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class ModificadorFormatos {

	/**
	 * 
	 * @param campo
	 * @param valor
	 * @return
	 */
	public static String formatea(final Campo campo, final String valor) {
	
		String ret = "";
	
		if ("A".equals(campo.getTipo())) {
			ret = Constantes977R.COMILLAS + valor + Constantes977R.COMILLAS;
		} else if ("D".equals(campo.getTipo())) {
			ret = formateaCampoDuracion(valor);
		} else if ("F".equals(campo.getTipo())) {
			ret = formateaCampoFecha(valor);
		} else if ("H".equals(campo.getTipo())) {
			ret = formateaCampoHora(valor);
		} else if ("I".equals(campo.getTipo())) {
			ret = formateaCampoNumerico(campo, valor);
		} else if ("N".equals(campo.getTipo())) {
			ret = formateaCampoNumerico(campo, valor);
		}
		return ret;
	}

	/**
	 * @param valor
	 * @return
	 */
	private static String formateaCampoDuracion(final String valor) {
		
		String sec = valor.substring(valor.length() - 2);
		String min = valor.substring(valor.length() - 4, valor.length() - 2);
		String hor = valor.substring(0, valor.length() - 4);
		double duracion = Double.valueOf(hor).doubleValue() * 60
				+ Double.valueOf(min).doubleValue()
				+ Double.valueOf(sec).doubleValue() / 60;
		Locale locale = Locale.getDefault();
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
		otherSymbols.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat("#0.0000", otherSymbols);
		
		return df.format(duracion);
	}

	/**
	 * @param valor
	 * @return
	 */
	private static String formateaCampoFecha(final String valor) {
		String ret;
		String dia = valor.substring(valor.length() - 2);
		String mes = valor.substring(valor.length() - 4, valor.length() - 2);
		String any = valor.substring(0, valor.length() - 4);
		ret = any + "-" + mes + "-" + dia;
		ret = any + mes + dia;
		return ret;
	}

	/**
	 * @param valor
	 * @return
	 */
	private static String formateaCampoHora(final String valor) {
		String ret;
		String sec = valor.substring(valor.length() - 2);
		String min = valor.substring(valor.length() - 4, valor.length() - 2);
		String hor = valor.substring(0, valor.length() - 4);
		ret = hor + ":" + min + ":" + sec;
		return ret;
	}

	/**
	 * @param campo
	 * @param valor
	 * @return
	 */
	private static String formateaCampoNumerico(final Campo campo, final String value) {
		String val = "";
		String valor = corrigeCamposNumericos(value);
		int posDecimal = campo.getFormato().indexOf(",");
		int numDecimales = 0;
		if (posDecimal > -1) {
			numDecimales = Integer.valueOf(campo.getFormato().substring(posDecimal + 1));
			val = valor.substring(0, valor.length() - numDecimales) + "."
					+ valor.substring(valor.length() - numDecimales);
			double dNumerico = Double.valueOf(val);
			
			Locale locale = Locale.getDefault();
			DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(locale);
			otherSymbols.setDecimalSeparator('.');
			DecimalFormat df = new DecimalFormat("#0.0000", otherSymbols);
			val = df.format(dNumerico);
		} else if(campo.getLongitud() > 4 && campo.getLongitud() < 9){ //Código postal, códigos varios
			val = Constantes977R.COMILLAS + valor + Constantes977R.COMILLAS;
		}else{
			//logger.info(valor + ":" + campo.toString());
			long l = Long.valueOf(valor);
			NumberFormat nf = new DecimalFormat("#");
			val = nf.format(l);
		}
		return val;
	}

	private static String corrigeCamposNumericos(final String valor) {
		String positivos = "{ABCDEFGHI";
		String negativos = "}JKLMNOPQR";
		String val = valor;
	
		String derecha = val.substring(val.length() - 1);
		if (positivos.indexOf(derecha) > -1) { // es positivo
			val = val.replace(derecha,
					(new Integer(positivos.indexOf(derecha))).toString());
		} else if (negativos.indexOf(derecha) > -1) { // es negativo
			val = valor.replace(derecha,
					(new Integer(negativos.indexOf(derecha))).toString());
			val = "-" + val;
		}
		return val;
	}

}
