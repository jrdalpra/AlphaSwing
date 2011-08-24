- usar o que já existe
- apenas criar facilitadores para que não seja necessário "gastar" tanto tempo
- convenção sobre configuração
- usar o máximo possível de frameworks já existentes. apenas criar uma série de faciliadores
- usar conceito de "BeanDefinition" do Spring para poder "processar" várias vezes as propriedades
- criar um conjunto de processadores de propriedades 
- criar um conjunto de fabricas ( olhar como funciona o vraptor )
- criar um conjunto de processadores para executar métodos auxiliares no final da criação do componente
  por exemplo, chamar o "validate" do container para "repintar" a tela
- sempre usar "proxies" em todos os componentes, assim é possível intermediar a chamada de métodos
  ver se já tem algo mais fácil para gerar os proxies.

  0) Instanciar
  1) Injetar as dependencias ( é gerenciado pelo Container? )
  2) temos que que reconhecer quais são os componentes existentes
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
		@Property(name="title",value="{pais.classe}"), // buscar de um resourcebundle padrão
		@Property(name="size",value="[300,300]"), /*@Property(name="size",value="[{largura.padrao},{altura.padrao}]"),*/
		@Property(name="visible",value="true")
	 })
public class PaisForm extends JPanel
{

	@Inject // CDI
	private PaisService service;
	
	@BindGroup({
		@Bindable(source="pais.codigo",mutual=true) // sabe que a property padrão é "text"
		@Bindable(source="box.selected",property="enabled")
	})
	private JTextField codigo;
	
	private JTextField outroCampo;
	
	@Bindable(source="box.selected",property="enabled") // está habilitado qdo o checkbox está selecionado
	private JTextField name;
	
	private JCheckBox box;
	
	@Properties({@Property(name="label",value="{acao.salvar}")})
	@Action(method="aoClicarNoBotaoSalvar")
	@Icon("classpath:META-INF/images/buttons/salvar.png")
	private JButton salvar;
	
	@Properties({@Property(name="label",value="{acao.cancelar}",
		@Property(name="icon",value="{images.button.cancelar}") // 1o mudar o valor para o conteudo do resource bundle, depois carrega icone
	private JButton cancelar; // action padrão onButtonCancelarPressed
	
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
		setLayout(form(label("Código"),label("Nome")).rowGap("2px")); /* formlayout, já diz os labels e o gap entre as linhas */
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
			// validação na parte cliente
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

//Versão com configuracao

public LoginFrame extends JFrame {

   @Bindable
   private Usuario usuario;

   @Inject
   @Bind(property="text", value="usuario.login", mutual=true)
   @ValidateBasedOn("usuario.login")
   @MaskBasedOn("usuario.login")
   private JTextField login

   @Inject
   @ValidateBasedOn("usuario.senha")
   @MaskBasedOn("usuario.senha")
   private JTextField senha;

   @Inject
   @Style(sclass="okbutton")
   @Bind(property="enabled","${senha.enabled}")
   @Action(name="onOk")
   private JButton ok;

   public void initUI(){

      //BindingUtils.bind(login,"text",usuario,"login",true); // mesmo que @Bind
      //BindingUtils.bind(senha,"enabled","${login.text.length > 0}");  
      //BindingUtils.bind(ok,"enabled","${senha.enabled}");

      new FormLayout(2,3).labelsOn(LEFT)
                     .with(new FormItem("Login",login).widget(FILL))
                     .with(new FormItem("Senah",senha))
                     .on(this);  
   }

   @Action(label="${tradutor.traduz('Entrar')}",key=Key.Enter,ctrl=true)
   public void onOk(Action event){
   }

   public void setUsuario(Usuario novo){
      this.usuario = BindableObject.bind(novo);
   }

   public Usuario getUsuario(){
      return ((BindableObject)usuario).getTarget();
   }
}


// Versaon com convensões

@Component
@MenuItem
public LoginFrame extends JFrame{
   
   @Getter // lombok
   @Setter // lombok
   @Bindable
   private Usuario usuario;
   
   @Inject
   private JTextField usuario_login;
   
   @Inject
   private JTextField usuario_senha;
   
   private JButton ok;
   
   public void initUI(){      
      new Form().with(new FormItem("Login",usuario_login).widgethWidth(FILL))
              .with(new FormItem("Senha",usuario_senha))
              .with(new FormItem(null,ok).columnSpan(2))
              .labelsOnX(LEFT)
              .on(this);   
   }

   public void onOk(ActionEvent event){
   }
   
}



