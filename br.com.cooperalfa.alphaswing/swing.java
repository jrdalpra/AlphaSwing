- usar o que j� existe
- apenas criar facilitadores para que n�o seja necess�rio "gastar" tanto tempo
- conven��o sobre configura��o
- usar o m�ximo poss�vel de frameworks j� existentes. apenas criar uma s�rie de faciliadores
- usar conceito de "BeanDefinition" do Spring para poder "processar" v�rias vezes as propriedades
- criar um conjunto de processadores de propriedades 
- criar um conjunto de fabricas ( olhar como funciona o vraptor )
- criar um conjunto de processadores para executar m�todos auxiliares no final da cria��o do componente
  por exemplo, chamar o "validate" do container para "repintar" a tela
- sempre usar "proxies" em todos os componentes, assim � poss�vel intermediar a chamada de m�todos
  ver se j� tem algo mais f�cil para gerar os proxies.

  0) Instanciar
  1) Injetar as dependencias ( � gerenciado pelo Container? )
  2) temos que que reconhecer quais s�o os componentes existentes
  3) Definir properties dos componentes declarados
  4) Criar binding entre os componentes
  5) Chamar "propriedades" "LATER", ou seja, que devem ser chamadas depois que foi definido o layout
      setVisible(true);

- como resolver o problema do binding bidirecional ( tem que saber da onde veio e parar a propagacao para nao "voltar" ao mesmo componente)

  
@Entity
@Bindable
class Pais {
	
	@Id
	@Mask("999.999")
	@Formatter(NumberFormatter.class)
	private Long codigo;

	@NotNull
	@NotEmpty
	@Length(min=2,max=120)
	private String nome;
	// getters , setter ( lombok )
}

@Properties({
		@Property(name="title",value="{pais.classe}"), // buscar de um resourcebundle padr�o
		@Property(name="size",value="[300,300]"), /*@Property(name="size",value="[{largura.padrao},{altura.padrao}]"),*/
		@Property(name="visible",value="true")
	 })
public class PaisForm extends JPanel
{

	@Inject // CDI
	private PaisService service;
	
	@BindGroup({
		@Bindable(source="pais.codigo",mutual=true) // sabe que a property padr�o � "text"
		@Bindable(source="box.selected",property="enabled")
	})
	private JTextField codigo;
	
	private JTextField outroCampo;
	
	@Bindable(source="box.selected",property="enabled") // est� habilitado qdo o checkbox est� selecionado
	private JTextField name;
	
	private JCheckBox box;
	
	@Properties({@Property(name="label",value="{acao.salvar}")})
	@Action(method="aoClicarNoBotaoSalvar")
	@Icon("classpath:META-INF/images/buttons/salvar.png")
	private JButton salvar;
	
	@Properties({@Property(name="label",value="{acao.cancelar}",
		@Property(name="icon",value="{images.button.cancelar}") // 1o mudar o valor para o conteudo do resource bundle, depois carrega icone
	private JButton cancelar; // action padr�o onButtonCancelarPressed
	
	private Pais pais;
	
	@MigLayoutConstraint(layoutConstraint={},
						 columnsConstraint={@MigLayoutColumn(width="30%"),
											@MigLayoutColumn(width="100%")},
						 rowConstraint={})
	private MigLayout layout;

	//public PaisForm(PaisService service, PaisValidator validator){
	public PaisForm(PaisService service, BeanValidator validator){
		this.service = service;
		this.validator = validator;
		this.validator.view(this);
	}
	
	@PostConstruct
	public void init(){
		setLayout(form(label("C�digo"),label("Nome")).rowGap("2px")); /* formlayout, j� diz os labels e o gap entre as linhas */
		add(codigo,constraint(1,2,PREFFERED,PREFFERED)); // linha 1, coluna 2, largura e altura preferencial
		add(name,constraint(2,2,FILL,PREFFERED); // linha 2 , coluna 2, largura preenche e altura preferencial
	}
		
	// todo, permitir que o usuario mude o "padrao" de metodos "action"	
	public void onButtonCancelarPressed(...){
		clear(this);
		close(this);			// metodo static de classe utilitaria
	}
	
	public void aoClicarNoBotaoSalvar(){
		try{
			// valida��o na parte cliente
			if( validator.isValid(pais) && confirm('Deseja salvar?') ){
				service.salva(pais);
				clear(this);			// metodo static de classe utilitaria
			}			
		} catch(Exception exception){
			show(exception); // mostrar erro usando classe utilitaria
		}
	}

	public static void main(String[] args) {
		form = ... // usar CDI
	}
}

	// DSL
	public PaisForm(){
		Button salvar = define(button("{salvar}")).listeners(pressed(this,"salvar"))
												   .constraint(CENTER,BOTTOM,PREFFERED,PREFFERED);
		Frame frame = define(frame())
						.title("{pais.classe}")
						.layout(relative()) // flowx flowy grid ...
				        .children(
							define(label("{pais.codigo}")).constraint(LEFT,TOP,FILL,PREFFERED),
						   	define(input(numeric(10,0))).constraint(AFTER,SAME,PREFFERED,PREFFERED)
														.bind(this.pais,"codigo",READ-WRITE)
														.validate(bean(Pais.class,"codigo")
														.mask("999.999"),
								 define(frame()).border(titled("UFs")).constraint(AFTER,SAME,PREFFERED,FIT,salvar).children(...));
		add(frame);
	}	
	
	public class PaisForm extends Frame {
	
		public PaisForm(){
			title("{pais.classe}");
			layout(relative());
		}
	
	}
	
	// Groovy
	
	class PaisForm {
		new SwingBuilder().edt{
			frame(title:"{pais.classe}"){
			
			}
		}
	
	}
	
	
	
	
	
}

